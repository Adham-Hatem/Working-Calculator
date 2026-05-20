# 🧮 Advanced Calculator

A Java-based console calculator that supports advanced mathematical operations, expression parsing, and history management.

---

## ✨ Features

### ➕ Basic Operations
- Addition (`+`)
- Subtraction (`-`)
- Multiplication (`*`)
- Division (`/`)
- Modulus (`%`)
- Power (`^`)

### 🔬 Advanced Operations
- Roots (`rt`)
- Logarithmic functions:
  - `log`
  - `ln`
- Trigonometric functions:
  - `sin`
  - `cos`
  - `tan`
- Bracket support using `(` and `)`

### 📜 History System
- Automatically saves calculations
- Displays previous calculations
- Clears history on command
- Circular overwrite system for latest entries

### ⚠️ Error Handling
Detects:
- Division by zero
- Invalid operators
- Incorrect operator placement
- Improper entries
- Root syntax errors
- Function errors
- Bracket mismatches
- Complex trigonometric cases

---

# 📂 Project Structure

```bash
📁 AdvancedCalculator
 ┣ 📄 AdvancedCalculator.java
 ┣ 📄 history.txt
 ┗ 📄 README.md
```

---

# 🚀 How to Run

## 1️⃣ Compile the Program

```bash
javac AdvancedCalculator.java
```

## 2️⃣ Run the Program

```bash
java AdvancedCalculator
```

---

# 🛠️ Available Commands

| Command | Description |
|---|---|
| `commands` | Shows all supported calculations |
| `history` | Displays calculation history |
| `clearhistory` | Clears saved history |
| `historyclear` | Alternative history clear command |
| `bye` | Exits the calculator |

---

# 🧠 Supported Expression Formats

## ➕ Basic Arithmetic

```text
5+3
10-2
4*7
20/5
15%4
2^5
```

---

## 🌱 Roots

### Format

```text
{degree}rt{number}
```

### Examples

```text
2rt25
3rt27
```

---

## 📐 Trigonometric Functions

```text
sin90
cos60
tan45
```

> Angles are calculated in **degrees**.

---

## 📈 Logarithmic Functions

```text
log100
ln2
```

---

## 🔢 Brackets

```text
(5+3)*2
2^(3+1)
```

---

# 💻 Example Session

```text
~ Commands:
 - Commands
 - History
 - Clear History
 - Bye

> Enter your Command: 5+5

        ~ Result: 10.0

> Enter your Command: sin90+5

        ~ Result: 6.0

> Enter your Command: history

     ~ History:
         - 5+5 = 10.0
         - sin90+5 = 6.0
```

---

# 📁 History File

The calculator automatically creates:

```text
history.txt
```

This file stores:
- Previous calculations
- Calculation results
- Current history placement index

---

# ❌ Error Messages

| Error | Meaning |
|---|---|
| `Zero Division` | Cannot divide by zero |
| `Placement Error` | Invalid operator placement |
| `Bracket Error` | Missing or mismatched brackets |
| `Function Error` | Invalid function usage |
| `Root Error` | Invalid root syntax |
| `Improper Entry` | Unsupported characters or empty input |

---

# ⚙️ Technical Details

| Method | Purpose |
|---|---|
| `calculate()` | Main expression parser |
| `calculateFunctions()` | Handles trig/log/root functions |
| `calculatePower()` | Handles exponentiation |
| `calculateModulus()` | Handles modulus operations |
| `calculateSimple()` | Handles basic arithmetic |
| `historyFunction()` | Saves calculations to history |
| `checkIfValid()` | Validates user input |

---

# 📌 Notes

- Trigonometric functions use **degrees**, not radians.
- History is limited to **5 entries** by default.
- Spaces are automatically removed before processing.
- Unsupported symbols or invalid syntax will trigger errors.

---

# 🔮 Future Improvements

Planned enhancements:
- Factorials
- Scientific notation
- Better operator precedence
- GUI version
- Variables and constants
- Memory functions
- Stack-based expression parser

---

# 👨‍💻 Author

Created in Java to practice:
- File handling
- Expression parsing
- Error handling
- Mathematical computations
- Console applications
