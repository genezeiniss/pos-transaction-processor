# POS Transaction Processor

A Point of Sale (POS) integrated e-commerce platform that calculates the final price and reward points based on payment method modifiers. Since some payment providers charge commission fees, the system must limit discounts when such payment methods are used to maintain profitability.

Designed for use across shops and online stores, which will integrate with the system for seamless transactions.

## Goals and Objectives
1. Compute the final price and reward points per transaction based on payment method modifiers.
2. Validate payment method-specific metadata.
3. Generate timezone-aware hourly sales reports.
4. Support easy extension and high concurrency.

## Tech Stack
- **Language**: Java
- **API Type**: GraphQL
- **Framework**: Spring Boot
- **Database**: PostgreSQL
- **Persistence**: Liquibase and JOOQ
- **Build Tool**: Maven
- **Containerization**: Docker
- **Testing**: JUnit5
- **Version Control**: Git (GitHub)

## Project Scope
### ✅ Included

- Reward points and final price calculation based on payment metadata.
- Input validation for different payment types.
- Secure and encrypted storage of sensitive metadata.
- Hourly reporting based on sales and points awarded.

### ❌ Excluded

- Actual payment processing.
- Integration with external payment gateways.
- User authentication (unless specified in the future).


## Core Features

### Transaction Modifier Engine
**Input Fields**:
- `customerId`, `price`, `priceModifier`, `paymentMethod`, `datetime`, `additionalItem`

**Validation Rules**:

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

**Business Logic**:
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

**Storage**:
- Transactions are saved with all details (including requester / user_id).
- Sensitive fields (e.g., `bank_account_number`, `cheque_number`) are encrypted.

### Sales Report
**Input**:
- `startDateTime`, `endDateTime`

**Output**:
- Hourly breakdown of sales and points for specific user.

## Application Setup Guide (Development)

### Prerequisites

* Podman (or Docker) installed
* Java 21 JDK
* Maven

### Quick Start

1. Clone the repository

```bash
   git clone https://github.com/genezeiniss/pos-transaction-processor.git
```

```bash
   cd {project directory}
```

2. Make the script executable

```bash
   chmod +x start-dev.sh
```

3. Run the development environment
```bash
   ./start-dev.sh
```

This will automatically:

* Start a PostgreSQL container
* Run database migrations (Liquibase)
* Generate jOOQ classes
* Launch the Spring Boot application

### Graphql Playground:
```
  http://localhost:8080/graphiql?path=/graphql
```