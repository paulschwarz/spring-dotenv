# Changelog

## [Unreleased]

### ✨ Enhancements

_Relaxed binding for `springdotenv.*` is Spring Boot–only; relaxed resolution of `.env` entries applies to both Spring Boot and Spring Framework._

#### Relaxed binding for `springdotenv.*` configuration

- Full **Spring Boot–style relaxed binding** is now supported for all `springdotenv.*` configuration keys.
- Configuration can be supplied interchangeably via:  
  - kebab-case (`springdotenv.ignore-if-missing`)
  - camelCase (`springdotenv.ignoreIfMissing`)
  - uppercase environment variables (`SPRINGDOTENV_IGNORE_IF_MISSING`)
- Behavior is delegated to Spring Boot’s `Binder`, ensuring consistency with native Boot configuration rules.

#### Relaxed binding for `.env` file entries

- Keys defined inside `.env` files now participate in relaxed name resolution when exposed to Spring.
- This applies equally to Spring Boot and plain Spring Framework applications.
- This allows seamless access across common naming styles without duplicating entries in `.env`.
- Aligns dotenv value resolution with Spring Boot’s property resolution semantics.

#### Deterministic precedence between legacy and renamed keys

- When both legacy and new configuration keys are present:
  - **The new canonical key wins.**
  - Example:  
  `springdotenv.exportToSystemProperties` overrides the deprecated `springdotenv.systemProperties`.
- Legacy keys remain supported for compatibility, with clear precedence rules.

---

### 🧪 Testing

- Added focused smoke tests to validate:  
  - Relaxed binding across naming variants 
  - Partial configuration scenarios (defaults + overrides)
  - Correct precedence between legacy and canonical keys
- Tests explicitly avoid global system property pollution.

---

### 🧱 Internal changes

- Introduced a Boot-specific relaxed configuration loader that:
  - Reuses Spring Boot’s binding infrastructure 
  - Avoids re-implementing relaxed-binding logic 
- Clear separation between:
  - **Spring-only behavior** (exact keys)
  - **Spring Boot behavior** (relaxed binding via `Binder`)

## [5.0.1] – 2025-12-23

### 🚨 Breaking Changes

#### Configuration renames

- **`springdotenv.systemProperties` → `springdotenv.exportToSystemProperties`**  
  The configuration key (and corresponding environment variable
  `SPRINGDOTENV_SYSTEM_PROPERTIES`) has been replaced by
  `springdotenv.exportToSystemProperties` /
  `SPRINGDOTENV_EXPORT_TO_SYSTEM_PROPERTIES`.

  Update application properties, environment variables, and any scripts or
  documentation that control exporting dotenv values into system properties.

  `systemProperties` is deprecated but still supported with warning.

#### Artifact split & coordinates

The project is now published as multiple artifacts. Consumers must choose the
appropriate module:

- `me.paulschwarz:spring-dotenv`  
  Core library for Spring Framework (no Spring Boot integration)

- `me.paulschwarz:springboot3-dotenv`

- `me.paulschwarz:springboot4-dotenv`

Older single-artifact setups must migrate to the correct module.  
**Spring Boot 2 integration is no longer provided.**

#### Version alignment via BOM

- Introduced **`me.paulschwarz:spring-dotenv-bom`** to manage versions across all
  modules.
- Consumers should import the BOM (`platform(...)` in Gradle,
  `<dependencyManagement>` in Maven).
- Pinning individual module versions directly is now discouraged.

#### Java baseline

- The Java toolchain now targets **Java 17**.
- Java 8 and Java 11 are no longer supported.
- This aligns with Spring Boot 3+, which itself requires Java 17.

#### Prefix handling

- Prefix-based access is no longer supported.
- The library now defaults to **no prefix**.
- Applications should migrate to unprefixed property access.

---

### ✨ New & Notable Additions

#### Spring integration kill switch

- New configuration option: **`springdotenv.enabled`**
  (or `SPRINGDOTENV_ENABLED`)
- Defaults to `true`
- Set to `false` to bypass loading `.env` values into the Spring `Environment`

This allows explicit opt-out when using the core library or custom integration.

#### Spring Boot 3 and 4 support

- Added dedicated **`springboot3-dotenv`** and **`springboot4-dotenv`** modules.
- Includes a Boot 3 and a Boot 4–compatible environment post-processor respectively.

---

### 🧱 Build, Tooling & Infrastructure

- Gradle upgraded to 9.2.1.
- Java toolchain configuration centralized in build logic.
- Error Prone checks reintroduced.
- CI/CD workflows modernized and consolidated.
    - Clear separation between CI, snapshot publishing, and tagged releases.
    - Default branch renamed to `main`.
    - Improved snapshot handling.

---

### 📦 Installation

**Recommended approach**

1. Import `me.paulschwarz:spring-dotenv-bom`
2. Depend on exactly one runtime module:
    - `spring-dotenv`, or
    - `springboot3-dotenv`, or
    - `springboot4-dotenv`

See `docs/release-install.md` for Maven and Gradle examples.

---

### 🧪 Testing

- Not run for this snapshot
- QA review only
