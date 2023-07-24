package core.basesyntax.service.impl;

import core.basesyntax.model.FruitTransaction;
import core.basesyntax.model.Operation;
import core.basesyntax.service.impl.service.ConvertService;
import core.basesyntax.strategy.OperationStrategy;
import java.util.List;
import java.util.stream.Collectors;

public class ConvertServiceImpl implements ConvertService {
    private static final String INCORRECT_DATA_EXCEPTION
            = "Incorrect operation type or file contains empty line!"
            + " Insert correct type to input file or delete empty line";
    private static final String LESS_THAN_ZERO_EXCEPTION
            = "Quantity cannot be negative! Insert correct values to input file";
    private static final String COMA_DELIMITER = ",";
    private static final int TYPE_INDEX = 0;
    private static final int FRUIT_INDEX = 1;
    private static final int QUANTITY_INDEX = 2;
    private static final String COLUMN_NAME = "type";
    private static final String INVALID_INPUT_PARAMETER
            = "Invalid input parameter in convertData()";
    private OperationStrategy operationStrategy;

    public ConvertServiceImpl(OperationStrategy operationStrategy) {
        this.operationStrategy = operationStrategy;
    }

    @Override
    public List<FruitTransaction> convertData(List<String> strings) {
        if (strings == null) {
            throw new RuntimeException(INVALID_INPUT_PARAMETER);
        }
        return strings.stream()
                .filter(line -> !line.startsWith(COLUMN_NAME))
                .map(this::getFruitTransactionObject)
                .collect(Collectors.toList());
    }

    private FruitTransaction getFruitTransactionObject(String line) {
        String[] row = line.split(COMA_DELIMITER);
        if (operationStrategy.get(row[TYPE_INDEX]) == null) {
            throw new RuntimeException(INCORRECT_DATA_EXCEPTION);
        }
        if (Integer.parseInt(row[QUANTITY_INDEX]) < 0) {
            throw new RuntimeException(LESS_THAN_ZERO_EXCEPTION);
        }
        Operation operation = operationStrategy.get(row[TYPE_INDEX]).getOperationType();
        String fruit = row[FRUIT_INDEX];
        int quantity = Integer.parseInt(row[QUANTITY_INDEX]);
        return new FruitTransaction(operation, fruit, quantity);
    }
}