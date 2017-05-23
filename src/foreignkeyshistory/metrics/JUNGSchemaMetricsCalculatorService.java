package foreignkeyshistory.metrics;

import foreignkeyshistory.Schema;

public class JUNGSchemaMetricsCalculatorService extends SchemaMetricsCalculatorService{

	@Override
	public SchemaMetricsCalculator getSchemaMetricsCalculator(Schema schema) {
		return new JUNGSchemaMetricsCalculator(schema);
	}

}
