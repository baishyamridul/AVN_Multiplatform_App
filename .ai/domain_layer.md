Domain Layer

Domain contains business knowledge.

Each business concept owns its own module.

Example

domain/farmer

contains

* Farmer
* FarmerRepository
* GetFarmerUseCase
* SaveFarmerUseCase

Domain owns

* Business Models
* Repository Interfaces
* Use Cases
* Business Rules

Domain never contains

* DTO
* Entity
* SQL
* API
* Compose UI
* ViewModels

⸻