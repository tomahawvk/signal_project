## Part 4 

This implementation applies four design patterns to the patient storage and alert system.
Factory Method is implemented through `AlertFactory` and concrete factories: `BloodPressureAlertFactory`, `BloodOxygenAlertFactory`, `ECGAlertFactory`, and `ManualAlertFactory`. These factories create specific alert types without hardcoding construction logic into the alert evaluator.
Strategy is implemented through `AlertStrategy` and concrete strategies: `BloodPressureStrategy`, `OxygenSaturationStrategy`, `HeartRateStrategy`, and `ManualAlertStrategy`. Each strategy encapsulates one group of alert-checking rules, keeping `AlertGenerator` modular and easier to extend.
Decorator is implemented through `AlertDecorator`, `PriorityAlertDecorator`, and `RepeatedAlertDecorator`. These decorators add priority and repeat behavior to alerts without changing the base alert classes.
Singleton access is implemented through `getInstance()` methods in `DataStorage` and `HealthDataSimulator`.
All unit tests pass, including tests for factories, decorators, strategies, singleton access, alert generation, file reading, and patient record retrieval.