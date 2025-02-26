# Banking API using Spring Boot

This application can create/delete users, withdraw and deposit money into/from their accounts. This API is created using JWT Security and thus a token must be generated to access the user. Each bearer token is valid for one hour. All the data is saved in a mysql / mariadb database

Here are the API endpoints

Welcome
---
An unauthorised endpoint to test that the AI is working properly
```http
GET /welcome
```
Creating Users
---
```http
POST /addNewUser
```
#### Raw body
```json
{
    "username" : "TestUser",
    "password" : "password",
    "balance" : 20
}
```

Generating token
---
All REST API endpoints below this should be using the token generated as the bearer authorisation
```http
POST /generateToken
```
#### Raw Body
```json
{
    "username" : "TestUser",
    "password" : "password"

}

```

User Profile
---
```http
GET /user/profile
```
#### Returns a json response
eg.

```json
{
    "id": 3,
    "username": "TestUser",
    "password": "...",
    "balance": 48950.0,
    "createdAt": "2",
    "transactions": [
        {
            "id": 4,
            "fromUsername": "TestUser",
            "toUsername": "TestUser",
            "type": "withdraw",
            "amount": 1000.0,
            "time": ""
        }
    ]
}

```

Deposit
---
Deposits money into the account

```http
POST /user/deposit?amount={amount}
```
#### Response
```
Successfully deposited!
```

Withdraw
---
Withdraws money from the account

```http
POST /user/withdraw?amount={amount}
```
#### Response
```
Successfully withdrawn!
```
However in a scenario where there is not enough money from the user, a response as such would be returned
```
Insufficient balance
```

Transfer
---
Transfer money from one account to another
```http
POST /transfer?toAccountName=TestUser&amount=20
```

#### Response
```
Successfully transferred
```
Likewise if there is insufficient money from the sender
```
Insufficient balance
```

Delete
---
Deleting an account would not delete the transaction information associated with it

```http
DEL /user/delete
```

#### Response
```
Successfully deleted user
```

View Transactions
---
View the transactions regarding the user, and optionally add a filter to the tranfer types
```http
GET /user/getTransactions
```
Returns all the transactions

Optionally apple a filter

```http
GET /user/getTransactions?filterTransaction=transfer
```

Response
```json
[
    {
        "id": 7,
        "fromUsername": "TestUser1",
        "toUsername": "TestUser2",
        "type": "transfer",
        "amount": 1000.0,
        "time": ""
    }
]
```