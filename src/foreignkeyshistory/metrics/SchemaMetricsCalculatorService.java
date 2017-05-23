package foreignkeyshistory.metrics;

import foreignkeyshistory.Schema;

public abstract class SchemaMetricsCalculatorService {
	public abstract SchemaMetricsCalculator getSchemaMetricsCalculator(Schema schema);
}
