terraform {
  required_providers {
    aws = { source = "hashicorp/aws", version = ">= 4.0" }
    random = { source = "hashicorp/random", version = ">= 3.0" }
  }
}

provider "aws" {
  region = var.aws_region
}

// Genera una contraseña segura para el master user
resource "random_password" "master" {
  length           = 16
  special          = true
  override_special = "@"
}

// Security Group para la base de datos
resource "aws_security_group" "db" {
  name        = "${var.project}-${var.environment}-db-sg"
  description = "Allow MySQL access from API"
  vpc_id      = var.vpc_id

  ingress {
    description     = "MySQL from API SG"
    from_port       = 3306
    to_port         = 3306
    protocol        = "tcp"
    security_groups = [var.api_sg_id]
  }

  egress {
    description = "Allow all outbound"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name        = "${var.project}-${var.environment}-db-sg"
    Environment = var.environment
  }
}

// RDS MySQL administrado
module "db" {
  source  = "terraform-aws-modules/rds/aws"
  version = "~> 6.0"

  identifier             = "${var.project}-${var.environment}-mysql"
  engine                 = "mysql"
  engine_version         = "8.0"
  instance_class         = "db.t3.micro"
  allocated_storage      = 20
  max_allocated_storage  = 20
  storage_encrypted      = true
  multi_az               = false
  deletion_protection    = false

  username               = var.db_username
  password               = random_password.master.result
  db_name                = var.db_name
  port                   = 3306

  vpc_security_group_ids = [aws_security_group.db.id]
  subnet_ids             = var.private_subnet_ids

  backup_retention_period    = 7
  backup_window              = "03:00-04:00"
  maintenance_window         = "sun:04:00-sun:05:00"
  auto_minor_version_upgrade = true

  enabled_cloudwatch_logs_exports = ["error", "slowquery", "general"]
  monitoring_interval             = 0

  create_db_parameter_group = false
  create_db_option_group    = false
}

// Guarda credenciales en Secrets Manager
resource "aws_secretsmanager_secret" "db" {
  name = "${var.project}/${var.environment}/db"
}

resource "aws_secretsmanager_secret_version" "db" {
  secret_id     = aws_secretsmanager_secret.db.id
  secret_string = jsonencode({
    username = var.db_username
    password = random_password.master.result
    host     = module.db.db_instance_endpoint
    port     = module.db.db_instance_port
    dbname   = var.db_name
  })
}

// Output R2DBC URL para Spring Boot
output "r2dbc_url" {
  description = "R2DBC URL para conectar Spring Boot con RDS MySQL"
  value       = "r2dbc:mysql://${module.db.db_instance_endpoint}:${module.db.db_instance_port}/${var.db_name}"
}
