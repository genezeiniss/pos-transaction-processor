# POS Transaction Processor

A Point of Sale (POS) integrated e-commerce platform that calculates final prices and reward points based on payment
method modifiers. Since some payment providers charge commission fees, the system limits discounts for these methods to
maintain profitability.

Designed for integration across physical stores and online platforms for seamless transactions.

---
## Table of Contents

- [Goals and Objectives](#goals-and-objectives)
- [Tech Stack](#tech-stack)
- [Project Scope](#project-scope)
- [Core Feature: Transaction Modifier Engine](#core-feature-transaction-modifier-engine)
- [Application Setup Guide](#application-setup-guide)
- [Running Integration Tests](#running-integration-tests)
- [Next Steps](#next-steps)

---
## Goals and Objectives

1. Compute final price and reward points based on payment method modifiers.
2. Validate payment method-specific metadata.
3. Generate timezone-aware hourly sales reports (TBD).
4. Support easy extension and high concurrency.

---
## Tech Stack

- **Language**: Java
- **API Type**: GraphQL
- **Framework**: Spring Boot
- **Database**: PostgreSQL
- **Persistence**: Liquibase and JOOQ
- **Build Tool**: Maven
- **Testing**: JUnit5
- **Version Control**: GitHub

---
## Project Scope

### ✅ Included

- Reward points and final price calculation based on payment metadata.
- Input validation for different payment types.

### ❌ Excluded

- Actual payment processing.
- Integration with external payment gateways.
- User authentication (unless specified in the future).

---
## Core Feature: Transaction Modifier Engine

### Input Fields

`customerId`, `price`, `priceModifier`, `paymentMethod`, `datetime`, `additionalItem`

### Validation Rules

| Payment Method       | Required Fields               | Valid Modifier Range       |
|----------------------|-------------------------------|----------------------------|
| CASH                 | -                             | 0.9 ≤ x ≤ 1.0              |
| CASH_ON_DELIVERY     | `courier` (YAMATO/SAGAWA)     | 1.0 ≤ x ≤ 1.02             |
| VISA                 | `last4` (4-digit string)      | 0.95 ≤ x ≤ 1.0             |
| MASTERCARD           | `last4` (4-digit string)      | 0.95 ≤ x ≤ 1.0             |
| AMEX                 | `last4` (4-digit string)      | 0.98 ≤ x ≤ 1.01            |
| JCB                  | `last4` (4-digit string)      | 0.95 ≤ x ≤ 1.0             |
| LINE_PAY             | -                             | Must be exactly 1.0        |
| PAYPAY               | -                             | Must be exactly 1.0        |
| POINTS               | -                             | Must be exactly 1.0        |
| GRAB_PAY             | -                             | Must be exactly 1.0        |
| BANK_TRANSFER        | `bank`, `accountNumber`       | Must be exactly 1.0        |
| CHEQUE               | `bank`, `chequeNumber`        | 0.9 ≤ x ≤ 1.0              |

### Business Logic

- `finalPrice = price × priceModifier`
- `points = price × method-specific multiplier`

| Payment Method       | Points Multiplier |
|----------------------|-------------------|
| CASH                 | 0.05              |
| CASH_ON_DELIVERY     | 0.05              |
| VISA                 | 0.03              |
| MASTERCARD           | 0.03              |
| AMEX                 | 0.02              |
| JCB                  | 0.05              |
| LINE_PAY             | 0.01              |
| PAYPAY               | 0.01              |
| POINTS               | 0                 |
| GRAB_PAY             | 0.01              |
| BANK_TRANSFER        | 0                 |
| CHEQUE               | 0                 |

### Storage

- Transactions are saved with all details.
- Sensitive fields such as `bankAccountNumber` and `chequeNumber` are **encrypted** (TBD).

---
## Application Setup Guide

### Prerequisites

- [Java 21 JDK](https://adoptium.net/en-GB/temurin/releases/)
- [Maven](https://maven.apache.org/download.cgi)
- [Podman](https://podman.io/) or [Docker](https://www.docker.com/)

### Quick Start
1. Clone the repository

```bash
   git clone https://github.com/genezeiniss/pos-transaction-processor.git
```

```bash
   cd pos-transaction-processor
```

2. Make the script executable

```bash
   chmod +x start-dev.sh
```

3. Run the development environment
```bash
   ./start-dev.sh
```

This will:

* Start a PostgreSQL container
* Run database migrations (Liquibase)
* Generate jOOQ classes
* Launch the Spring Boot application

4. Open [GraphQL playground](http://localhost:8080/graphiql?path=/graphql)

---

## Running Integration Tests

To execute integration tests (classes ending with IT):

### Option 1: Using the development script (recommended)

```bash
   ./start-dev.sh --test
```

This will:

* Start a dedicated test PostgreSQL container (on port 5433)
* Run migrations on the test database
* Generate jOOQ classes for test environment
* Execute all integration tests
* Clean up the test container automatically

### Option 2: Manual execution

```bash
   mvn verify -Pintegration-tests 
```
---

## Next Steps

1. [ ] Enhance `AdditionalItem` to securely handle fields like `bankAccountNumber` and `chequeNumber` by implementing
   field-level encryption.
2. [ ] Add robust input validation mechanisms at the API layer to guard against malformed or malicious inputs.
3. [ ] Implement rate limiting to control transaction throughput and prevent abuse.
4. [ ] Implement sales reporting using an optimized **materialized view** and window slicing strategy
5. [ ] Add retry mechanism and/or dead letter queue for handling failed transaction attempts gracefully.
6. [ ] Implement structured logging while ensuring no sensitive transaction metadata is logged.



