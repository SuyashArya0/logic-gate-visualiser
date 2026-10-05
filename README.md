# Circuit Visualiser

A desktop logic-gate simulator built with **JavaFX** and **Maven**. Drag gates onto a grid canvas, wire them together, click inputs to toggle them HIGH/LOW, and watch the signals propagate through your circuit in real time.

## Features

- **Interactive canvas** – drag-and-drop gate placement with 20 px grid snapping
- **Gate palette** – INPUT, OUTPUT, AND, OR, NOT, NAND, NOR, XOR, XNOR
- **Pin-to-pin wiring** – drag from a pin to create a connection, with a dashed preview while you drag
- **Bezier curved wires** – wires stay attached to their pins as you move nodes
- **Live signal visualisation** – active (HIGH) pins and wires show green; inactive (LOW) show grey
- **Click-to-toggle inputs** – click an INPUT node to flip it between 0 and 1
- **Evaluation engine** – iterative fixed-point propagation that handles chained gates and feedback loops (bounded at N + 1 passes)
- **Save / Load** – export and import circuits as JSON
- **Presets** – one-click Half-Adder example
- **Dark theme** – styled with external CSS

## Architecture

The project keeps the domain model free of any JavaFX code, so the simulation logic can be tested on its own.

| Layer | Package | Responsibility |
| --- | --- | --- |
| Model | `com.visualiser.model` | `LogicNode`, `Pin`, `Wire`, `GateType` – the circuit as a directed graph |
| Engine | `com.visualiser.engine` | `CircuitEvaluator` – propagates signals and re-evaluates gates until stable |
| View | `com.visualiser.view` | `CanvasPane`, `GateNodeView`, `PinView`, `WireView` – JavaFX rendering and interaction |
| Controller | `com.visualiser.controller` | `MainController` – wires the FXML layout to the canvas |
| Factory | `com.visualiser.factory` | `CircuitFactory` – preset circuits such as the Half-Adder |
| DTO / Util | `com.visualiser.dto`, `com.visualiser.util` | `CircuitData` and `StorageManager` – JSON serialisation with Jackson |

## Project Structure

```text
.
├── pom.xml
├── README.md
├── LICENSE
└── src
    ├── main
    │   ├── java/com/visualiser
    │   │   ├── App.java
    │   │   ├── controller/MainController.java
    │   │   ├── dto/CircuitData.java
    │   │   ├── engine/CircuitEvaluator.java
    │   │   ├── factory/CircuitFactory.java
    │   │   ├── model/{GateType,LogicNode,Pin,Wire}.java
    │   │   ├── util/StorageManager.java
    │   │   └── view/{CanvasPane,GateNodeView,PinView,WireView}.java
    │   └── resources
    │       ├── fxml/main_view.fxml
    │       └── styles/theme.css
    └── test
        └── java/com/visualiser/engine/CircuitEvaluatorTest.java
```

## Prerequisites

- JDK 17 or newer
- Apache Maven 3.8+

## Getting Started

```bash
git clone https://github.com/your-username/circuit-visualiser.git
cd circuit-visualiser

# Build
mvn clean compile

# Run
mvn exec:java -Dexec.mainClass="com.visualiser.App"
```

### Run the tests

```bash
mvn test
```

The `CircuitEvaluatorTest` suite (JUnit 5) checks the AND, XOR and NOT gate logic.

## Usage

1. **Add gates** – click a gate in the left-hand palette to place it on the canvas.
2. **Move gates** – drag a node; it snaps to the grid.
3. **Connect gates** – drag from an output pin (right side) to an input pin (left side).
4. **Toggle inputs** – click an INPUT node to switch it between LOW and HIGH.
5. **Try an example** – click **Load Half-Adder** in the toolbar.
6. **Save your work** – use **Save JSON** / **Load JSON**; **Clear Workspace** resets the canvas.

## Tech Stack

- Java 17+
- JavaFX (FXML + CSS)
- Maven
- Jackson (`jackson-databind`) for JSON
- JUnit 5 for tests

## Development Notes

**VS Code CSS warnings** – the default CSS linter flags JavaFX `-fx-*` properties. To silence them, add this to `.vscode/settings.json`:

```json
{
  "css.lint.unknownProperties": "ignore"
}
```

## Contributing

Contributions are welcome. Fork the repo, create a feature branch, and open a pull request. Please keep the model layer free of JavaFX imports and add tests for any change to evaluation logic.

## License

Released under the [MIT License](LICENSE).
