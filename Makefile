# Import local environment overrides
$(shell touch .env)
include .env

# Project variables
PROJECT_NAME ?= quote-generator
ORG_NAME ?= docker-production-aws
REPO_NAME ?= quote-generator

# Common settings
include Makefile.settings

.PHONY: version demo test build release clean tag login logout publish compose dcompose database save load

# Prints version
version:
	@ echo $(APP_VERSION)

# Creates workflow infrastucture
init:
	${INFO} "Checking networking..."
	@ $(if $(NETWORK_ID),,docker network create --subnet=$(NETWORK_SUBNET) --gateway=$(NETWORK_GW) $(NETWORK_NAME))

# Runs unit and integration tests
# Pulls images and base images by default
# Use 'make test nopull' to disable default pull behaviour
test: init
	${INFO} "Building images..."
	@ docker-compose $(TEST_ARGS) build $(NOPULL_FLAG) test
	${INFO} "Running tests..."
	@ docker-compose $(TEST_ARGS) up test
	${CHECK} $(TEST_PROJECT) $(TEST_COMPOSE_FILE) test
	${INFO} "Removing existing artefacts..."
	@ rm -rf target
	${INFO} "Copying build artefacts..."
	@ docker cp $$(docker-compose $(TEST_ARGS) ps -q test):/app/target/. target
	${INFO} "Test complete"

# Cleans environment
clean:
	${INFO} "Destroying test environment..."
	@ docker-compose $(TEST_ARGS) down -v || true
#	${INFO} "Destroying release environment..."
# @ docker-compose $(RELEASE_ARGS) down -v || true
	${INFO} "Removing dangling images..."
	@ docker images -q -f dangling=true -f label=application=$(REPO_NAME) | xargs -I ARGS docker rmi -f ARGS
	${INFO} "Clean complete"
