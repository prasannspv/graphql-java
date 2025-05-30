package graphql.schema.idl.errors;

import graphql.GraphQLException;
import graphql.PublicApi;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.TypeRuntimeWiring;

/**
 * An exception that is throw when {@link RuntimeWiring.Builder#strictMode(boolean)} or {@link TypeRuntimeWiring.Builder#strictMode(boolean)} is true and
 * something gets redefined.
 */
@PublicApi
public class StrictModeWiringException extends GraphQLException {
    public StrictModeWiringException(String msg) {
        super(msg);
    }
}
