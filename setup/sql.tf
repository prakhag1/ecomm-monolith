resource "google_compute_global_address" "private_ip_address" {
  provider = google-beta

  name          = "private-ip-address"
  purpose       = "VPC_PEERING"
  address_type  = "INTERNAL"
  prefix_length = 16
  network       = google_compute_network.vpc.id
}

resource "google_service_networking_connection" "private_vpc_connection" {
  provider = google-beta

  network                 = google_compute_network.vpc.id
  service                 = "servicenetworking.googleapis.com"
  reserved_peering_ranges = [google_compute_global_address.private_ip_address.name]
}

resource "google_sql_database_instance" "instance" {
  provider = google-beta

  name   = "${var.project_id}-mysql"
  region = "us-central1"

  depends_on = [google_service_networking_connection.private_vpc_connection]

  settings {
    tier = "db-f1-micro"
    ip_configuration {
      ipv4_enabled    = false
      private_network = google_compute_network.vpc.id
    }
  }
}

resource "google_sql_database" "database" {
  name     = "ecommerce"
  instance = google_sql_database_instance.instance.name
}

resource "google_sql_user" "users" {
  name     = "root"
  host     = "%"
  instance = google_sql_database_instance.instance.name
  password = "password"
}

provider "google-beta" {
  region = "us-central1"
  zone   = "us-central1-a"
}

output "sql_database_name" {
  value       = google_sql_database_instance.instance.name
  description = "CloudSQL Instance Name"
}
