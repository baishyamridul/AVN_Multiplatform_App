Feature Layer

A Feature represents a user workflow.

Good examples

* Login
* Dashboard
* Farmer Registration
* Farmer Details
* Survey Create
* Survey Details
* Reports
* Map

Bad examples

* Farmer
* User
* Crop

Those belong to Domain.

Each Feature owns

presentation/

* Screen
* ViewModel
* State
* Event
* Effect
* Components

di/

Features do NOT own

* Business Models
* Repository Implementations
* DTOs
* Entities

Features consume Domain.

⸻