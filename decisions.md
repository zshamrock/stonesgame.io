### Tue 15 Oct, 2015
- Introduced pool for the bots to reduce GC overhead

### Sun 25 Oct, 2015
- Decided to make bots behaviour configurable and use DropWizard EnvironmentVariableSubstitutor.
    The idea is to set more bots (about 100 via poolSize) and shorter scheduled period
    (about 10 sec via scheduledPeriodInSeconds) during initial launch, and then switch to smaller pool size and
    longer scheduled period.

