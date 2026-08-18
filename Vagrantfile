# Vagrantfile - provisions the Ubuntu VM used to run Asset Monitor.

# Run `vagrant up` from this directory. This single command creates the VM, installs
# Docker inside it, and starts it with Docker Compose.
# No manual setup steps required.

Vagrant.configure("2") do |config|
  config.vm.box = "ubuntu/jammy64"
  config.vm.hostname = "asset-monitor-vm"

  config.vm.provider "virtualbox" do |vb|
    vb.name = "asset-monitor-vm"
    vb.memory = 4096
    vb.cpus = 2
  end

  config.vm.network "forwarded_port", guest: 4200, host: 4200
  config.vm.network "forwarded_port", guest: 8080, host: 8080
  config.vm.network "forwarded_port", guest: 5280, host: 5280 # ejabberd admin UI
  config.vm.network "forwarded_port", guest: 5432, host: 5432 # Postgres (DB client access)

  # One-time setup: install Docker. Only runs on first `vagrant up`.
  config.vm.provision "shell", inline: <<-SHELL
    set -e

    # Add Docker's official GPG key:
    apt-get update
    apt-get install -y ca-certificates curl
    install -m 0755 -d /etc/apt/keyrings
    curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
    chmod a+r /etc/apt/keyrings/docker.asc

    # Add the repository to Apt sources:
    sudo tee /etc/apt/sources.list.d/docker.sources <<EOF
Types: deb
URIs: https://download.docker.com/linux/ubuntu
Suites: $(. /etc/os-release && echo "${UBUNTU_CODENAME:-$VERSION_CODENAME}")
Components: stable
Architectures: $(dpkg --print-architecture)
Signed-By: /etc/apt/keyrings/docker.asc
EOF

    apt-get update

    # Install the Docker packages:
    apt-get install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

    usermod -aG docker vagrant
  SHELL

  # Deploy step: runs on EVERY `vagrant up`.
  config.vm.provision "shell", run: "always", inline: <<-SHELL
    set -e

    cd /vagrant
    docker compose up --build -d

    echo "Stack is up. Visit http://localhost:4200 and http://localhost:8080/api/health from your host."
  SHELL
end
