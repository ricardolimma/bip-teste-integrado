# Sistema de Gerenciamento de Benefícios

Este é um sistema completo para gerenciar benefícios, desenvolvido com **Angular** no frontend e **Java (Spring Boot + EJB)** no backend. O sistema permite criar, listar, atualizar e deletar benefícios, além de realizar transferências de valores entre eles com validações de saldo e concorrência.

---

## Tecnologias Utilizadas

### Frontend
- Angular 16+ (standalone components)
- TypeScript
- RxJS
- FormsModule e CommonModule

### Backend
- Java 17+
- Spring Boot
- Spring Framework (Spring Data, Spring Security, Spring Batch)
- EJB (`BeneficioEjbService`)
- JPA / Hibernate
- Banco de dados: PostgreSQL ou MongoDB (configurável)
- Maven para build

### Testes
- JUnit 5
- Mockito
- Spring Boot Test (MockMvc para controllers)

---


---

## Funcionalidades

### Frontend
- Listar todos os benefícios
- Criar novo benefício
- Atualizar um benefício existente
- Deletar um benefício
- Mensagens de erro na tela quando as operações falham

### Backend
- API REST `/api/v1/beneficios`:
    - `GET /` → listar todos os benefícios
    - `GET /{id}` → buscar benefício por ID
    - `POST /` → criar novo benefício
    - `PUT /{id}` → atualizar benefício existente
    - `DELETE /{id}` → deletar benefício
- EJB `BeneficioEjbService`:
    - Transferência entre benefícios com validações de saldo
    - Tratamento de concorrência (`OptimisticLockException`)
    - Valida parâmetros (IDs e valores)
    - Operações CRUD sobre `Beneficio` usando JPA
- Validação de dados e tratamento de exceções customizadas:
    - `TransferException`
    - `InsufficientFundsException`
    - `AccountNotFoundException`
    - `ConcurrencyException`

---

## Pré-requisitos

- Node.js 20+
- npm 10+
- Angular CLI 16+
- Java 17+
- Maven 3+
- PostgreSQL

