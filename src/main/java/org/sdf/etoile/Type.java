package org.sdf.etoile;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.apache.spark.sql.catalyst.parser.CatalystSqlParser$;
import org.apache.spark.sql.types.DataType;

import java.io.Serializable;
import java.sql.Timestamp;

interface Type extends Serializable {

    DataType value();

    default String sql() {
        return value().catalogString();
    }

    default Class<?> klass() {
        if (sql().equals("string")) {
            return String.class;
        }
        if (sql().startsWith("timestamp")) {
            return Timestamp.class;
        }
        throw new UnsupportedOperationException();
    }

    @EqualsAndHashCode
    @ToString
    @RequiredArgsConstructor
    final class Of implements Type {
        private final DataType dataType;

        Of(final String type) {
            this(CatalystSqlParser$.MODULE$.parseDataType(type));
        }

        @Override
        public DataType value() {
            return this.dataType;
        }
    }
}
