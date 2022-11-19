game:
	@echo "Starting Generating Of World application"
	@java src/Main.java -> logs.log

dev:
	@echo "Starting..."
	@cd src/
	@java Main.java Window/Window.java
	@cd ../