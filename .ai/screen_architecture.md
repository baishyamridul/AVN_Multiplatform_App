Screen Architecture

Every screen consists of

Screen

↓

ViewModel

↓

UiState

↓

Event

↓

Effect

Each screen owns exactly one immutable UiState.

⸻

Forms

Forms use immutable state.

Do NOT keep dozens of mutableStateOf inside Composables.

Use

FormField

inside UiState.

Validation belongs inside ViewModel or UseCases.

⸻