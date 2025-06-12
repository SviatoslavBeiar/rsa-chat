# Secure Chat Web Application (Spring Boot + RSA)

A secure web-based chat application built using **Spring Boot**, enabling encrypted communication through **RSA** encryption.

## Features

- User Registration & Login
- One-on-One Chat Creation
- RSA Key Pair Generation per User
- Encrypted Messaging with RSA
- Encrypted Message Storage in Database

## Tech Stack

- **Backend:** Spring Boot, Spring Security, Spring Data JPA  
- **Database:** H2 (In-Memory)  
- **Frontend:** Thymeleaf (HTML Templates)  
- **Encryption:** RSA (2048-bit keys recommended)

## Implementation Notes

- RSA keys are generated during user registration  
- Messages are encrypted using the recipient's public key  
- Encrypted messages are stored in the database  
- Authentication via username/password (no email confirmation)  
- Basic chat history supported  
- Optional: Real-time chat with WebSockets, key regeneration, deletion of accounts/messages

## Future Considerations

- Should private keys be stored (securely encrypted) in the database?  
- Should chats support group conversations?  
- Who has access to public keys — all users or only chat participants?

---

## Future Enhancements

### 1. Ephemeral Messages (Self-Destructing)

Implement a timer-based system where messages automatically delete themselves from both client and server after being read or after a specified time.

- A countdown timer can be shown in the UI.
- Users may be allowed to extend the message lifespan, but only a limited number of times.
- Useful for temporary or highly sensitive communication.

### 2. Screenshot Detection

Add a "screenshot indicator" feature (similar to Signal):

- Detect when a screenshot is taken on mobile or desktop clients.
- Notify the chat participants about the event in real-time.

### 3. Server-Side Security & Crypto Architecture

- Refactor `ChatService` and `CryptoService` to remove RSA encryption responsibilities.
- Store only ciphertext, IV, and digital signatures in the database.
- Introduce HTTP endpoints for exchanging ECDH/ECDSA public keys.
- Use **HashiCorp Vault** (via `VaultTemplate`) or **Hardware Security Module (HSM)** for secure key storage.

### 4. Client-Side Cryptography

- All cryptographic operations (ECDH, AES-GCM, ECDSA) should be performed in the browser using the **WebCrypto API**.
- The server will only receive encrypted data and signatures — it will not handle plaintext or keys.

### 5. Metadata Minimization

- Avoid logging IP addresses, user-agent strings, or message timestamps.
- Option to fully disable non-essential server logs for maximum privacy.



