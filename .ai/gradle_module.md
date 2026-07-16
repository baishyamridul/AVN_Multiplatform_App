Gradle Modules

The application is a true Gradle Multi Module project.

Every top-level directory below is an independent Gradle module.

androidApp
desktopApp
iosApp
build-logic
core/
designsystem/
domain/
data/
feature/
sync/

Each subdirectory is also an independent module whenever appropriate.

Example

core/network
core/database
core/navigation
domain/farmer
domain/user
domain/weather
data/farmer
data/weather
feature/login
feature/dashboard
feature/map
feature/survey-create
feature/farmer-registration

Do NOT collapse these modules together.

⸻