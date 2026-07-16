Before Creating Any File

Determine

1. Which module owns this?

* Core
* DesignSystem
* Domain
* Data
* Feature

2. Is it

* Infrastructure
* Business
* Persistence
* UI

3. Which layer owns it?

* DTO
* Entity
* Domain Model
* Repository
* UseCase
* ViewModel
* State
* Event
* Effect
* Screen

If uncertain, stop and choose the correct module before generating code.

⸻

Important

Never violate this architecture unless explicitly instructed.

If a requested implementation conflicts with these rules, explain the conflict first and propose an architecture-compliant solution.

When generating new code, preserve the existing module boundaries and keep the dependency graph acyclic.