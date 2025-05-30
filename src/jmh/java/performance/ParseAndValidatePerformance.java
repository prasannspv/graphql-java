package performance;

import benchmark.BenchmarkUtils;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.ParseAndValidate;
import graphql.language.Document;
import graphql.parser.Parser;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.SchemaGenerator;
import graphql.validation.Validator;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static graphql.Assert.assertTrue;


@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 2, time = 5)
@Measurement(iterations = 3)
@Fork(3)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class ParseAndValidatePerformance {

    private static class Scenario {
        public final GraphQLSchema schema;
        public final ExecutionInput executionInput;

        Scenario(GraphQLSchema schema, ExecutionInput executionInput) {
            this.schema = schema;
            this.executionInput = executionInput;
        }
    }

    @State(Scope.Benchmark)
    public static class MyState {
        Scenario largeSchema1;
        Scenario largeSchema4;
        Scenario manyFragments;
        Scenario extraLargeSchema;

        @Setup
        public void setup() {
            largeSchema1 = load("large-schema-1.graphqls", "large-schema-1-query.graphql");
            largeSchema4 = load("large-schema-4.graphqls", "large-schema-4-query.graphql");
            manyFragments = load("many-fragments.graphqls", "many-fragments-query.graphql");
            extraLargeSchema = load("extra-large-schema-1.graphqls", "extra-large-schema-1-query.graphql");
        }

        private Scenario load(String schemaPath, String queryPath) {
            try {
                String schemaString = BenchmarkUtils.loadResource(schemaPath);
                String query = BenchmarkUtils.loadResource(queryPath);
                GraphQLSchema schema = SchemaGenerator.createdMockedSchema(schemaString);

                return new Scenario(schema, ExecutionInput.newExecutionInput(query).build());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    private void run(Scenario scenario) {
        ParseAndValidate.parseAndValidate(scenario.schema, scenario.executionInput);
    }

    @Benchmark
    public void largeSchema1(MyState state) {
        run(state.largeSchema1);
    }

    @Benchmark
    public void largeSchema4(MyState state) {
        run(state.largeSchema4);
    }

    @Benchmark
    public void manyFragments(MyState state) {
        run(state.manyFragments);
    }

    @Benchmark
    public void extraLargeSchema(MyState state) {
        run(state.extraLargeSchema);
    }
}
