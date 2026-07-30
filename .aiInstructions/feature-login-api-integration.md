
feature module ->
login (already exist)

domain module ->
user (already exist)

data module -> 
user (already exist)

api endpoint -> https://isam.sumato.tech/api/v1/login

request body ->
```json
{
"email" : "sumatoTester@gmail.com",
"password" : "secret",
"device_name" : "laptop"
}

```

success response ->
```json
{
  "status": 201,
  "message": "Token created",
  "data": {
    "token_type": "Bearer",
    "access_token": "133|wJqGaLvD552e7jo9jGwzUXA9PnxS80SutGSDzeLVb6a1842b",
    "user": {
      "type": "user",
      "id": "01kypqc5t4hqn1yjd5b4tcmppd",
      "attributes": {
        "name": "sumato tester",
        "email": "sumatoTester@gmail.com",
        "role": "distofficer",
        "phone": "7777222211",
        "photo": "https://api.dicebear.com/7.x/initials/svg?seed=sumato+tester&size=64",
        "designation": null,
        "created": {
          "human": "21 hours ago",
          "date": "2026-07-29",
          "formatted": "Jul 29, 2026"
        },
        "school": null
      },
      "relationships": [],
      "links": []
    }
  }
}
```

error response ->
```json
{
  "status": 422,
  "message": "The provided credentials are incorrect.",
  "errors": {
    "email": [
      "The provided credentials are incorrect."
    ]
  }
}
```

```json
{
  "status": 422,
  "message": "The email field is required.",
  "errors": {
    "email": [
      "The email field is required."
    ]
  }
}
```