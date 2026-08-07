# Setup: Local Dev + VM Deployment

## Workflow overview

- **Local machine**: day-to-day development (`ng serve`, `mvn spring-boot:run`) and
  pushing commits to GitHub.
- **VM (via Vagrant)**: a single `vagrant up` creates the VM, installs Docker and Git,
  clones this repo from GitHub, and starts it with Docker Compose.

This is the standard Vagrant pattern: a declarative, reproducible VM definition that
provisions itself, rather than manual setup steps.

## 1. Local development

### Backend (Spring Boot)

Requires JDK 21 and Maven.

```bash
cd backend
mvn spring-boot:run
```

Visit http://localhost:8080/api/health.

### Frontend (Angular)

Requires Node.js (20+).

```bash
cd frontend
npm install
npm start
```

Visit http://localhost:4200.

Commit and push changes to GitHub as normal.

## 2. Prerequisites for the VM

- VirtualBox: https://www.virtualbox.org/wiki/Downloads
- Vagrant: https://developer.hashicorp.com/vagrant/downloads

## 3. Point the Vagrantfile at your repo

Once you've created the GitHub repo and pushed the initial skeleton, open the
`Vagrantfile` and replace the placeholder `REPO_URL` with your actual repo's HTTPS URL,
e.g.:

```ruby
REPO_URL = "https://github.com/ivanrodrigues92/asset-monitor.git"
```

Using the plain HTTPS URL (rather than SSH) means no SSH key setup is needed inside the
VM, since this is a public repo.

## 4. Deploy with one command

```bash
vagrant up
```

This creates the VM, installs Docker and Git, clones the repo, and runs
`docker compose up --build -d`. Watch the provisioning output for a confirmation message
at the end.

## 5. Access from your host machine

Visit `http://localhost:4200` and `http://localhost:8080/api/health` directly -- the
forwarded ports make the containers inside the VM reachable from your host browser.

## 6. Redeploying after code changes

Push your changes to GitHub, then re-run provisioning so the VM pulls the latest code and
rebuilds:

```bash
vagrant provision
```

Or for a fully clean rebuild (useful to prove the whole setup is reproducible from
scratch):

```bash
vagrant destroy -f
vagrant up
```

## 7. Managing the VM

```bash
vagrant halt      # stop the VM
vagrant up        # start it again
vagrant ssh       # SSH into the VM if you want to poke around
vagrant destroy   # completely remove the VM
```
