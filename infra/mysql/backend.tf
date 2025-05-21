terraform {
  required_version = ">= 1.7"

  backend "s3" {
    bucket         = "franchise-terraform-state"   # bucket S3
    key            = "mysql/terraform.tfstate"    # ruta dentro del bucket
    region         = "us-east-2"                   # región
    dynamodb_table = "terraform-locks"             # DynamoDB
    encrypt        = true                          # cifra el state
  }
}