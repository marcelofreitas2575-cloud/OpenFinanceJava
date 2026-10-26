db = db.getSiblingDB('identity_db');
db.createCollection('users');

db = db.getSiblingDB('consent_db');
db.createCollection('consents');

db = db.getSiblingDB('account_db');
db.createCollection('accounts');

db = db.getSiblingDB('transaction_db');
db.createCollection('transactions');

db = db.getSiblingDB('mock_institution_db');
db.createCollection('accounts');
