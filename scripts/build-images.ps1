# scripts/build-images.ps1
#
# Runs on the HOST machine (via a Vagrant trigger, before the VM boots).
# Builds the backend and frontend Docker images locally, then exports them as .tar
# files. These tarballs are what actually get transferred into the VM -- the VM never
# needs internet access or the source code itself, only the pre-built images.
#
# Prerequisite: Docker Desktop installed and running on the host.

$ErrorActionPreference = "Stop"

Write-Host "== Building backend image =="
docker build -t asset-monitor-backend:latest ./backend

Write-Host "== Building frontend image =="
docker build -t asset-monitor-frontend:latest ./frontend

if (!(Test-Path -Path "build")) {
    New-Item -ItemType Directory -Path "build" | Out-Null
}

Write-Host "== Exporting backend image to build/backend.tar =="
docker save -o build/backend.tar asset-monitor-backend:latest

Write-Host "== Exporting frontend image to build/frontend.tar =="
docker save -o build/frontend.tar asset-monitor-frontend:latest

Write-Host "== Done. Images ready for offline transfer into the VM. =="
