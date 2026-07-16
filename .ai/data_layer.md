Data Layer

Data implements Domain.

Each Data module implements exactly one Domain module.

Example

data/farmer

contains

remote/

local/

mapper/

repository/

di/

Data owns

* Repository Implementations
* DTOs
* Database Entities
* API Clients
* DAOs
* Mappers

Repositories convert

DTO

↓

Domain Model

Entities

↓

Domain Model

Repositories always return Domain Models.

Never expose DTOs.

Never expose Entities.

⸻