INSERT INTO users (name, username, password, role)
VALUES ('master', 'master', '$2a$10$k17jgZLTy3JyjJ0oRTf./.VBCnLnSFYDUqFbhlTMdzM4cu1ncIFKS', 'ADMIN')
ON CONFLICT (username) DO NOTHING;