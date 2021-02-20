db.createUser(
    {
        user: "notificationUser",
        pwd: "Pa$$w0rd123",
        roles: [
            {
                role: "readWrite",
                db: "notificationdb"
            }
        ]
    }
);

db.a.insert([{
    "accountNumber": 1,
    "value": 1,
    "transactionType": "DEBIT",
    "created_date": {"$date": "2020-05-25T09:03:38.313Z"},
    "last_modified_date": {"$date": "2020-05-25T09:03:38.313Z"},
    "_class": "com.wallet.optimizer.transactionservice.domain.Transaction"
}]);
