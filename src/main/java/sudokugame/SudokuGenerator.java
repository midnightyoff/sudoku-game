package sudokugame;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;

public class SudokuGenerator {
    public static final int EASY = 42; // (42 to 51)
    public static final int MEDIUM = 32; // (32 to 41)
    public static final int HARD = 22; // (22 to 31)
    private final Random random = new Random();
    public ArrayList<ArrayList<Integer>> sudokuList = new ArrayList<>();
    public String sudokuAnswer = "";
    private long startTime;
    private int solutionCount = 0;
    public String MakeSudoku(int Level) {
        generateCompleteSudoku();
        // Содержит упорядоченные числа (0 до 80), если выбран сложный уровень
        // Иначе, содежрит неупорядоченные числа (0 to 80).
        ArrayList<Integer> AvailableIndexes = new ArrayList<>();
        if (Level == SudokuGenerator.HARD) {
            for (int i = 0; i < 81; i++) {
                AvailableIndexes.add(i);
            }
        } else {
            AvailableIndexes = getRandomValues(0, 80);
        }
        for (ArrayList<Integer> row : sudokuList) {
            for (Integer cell : row) {
                sudokuAnswer += cell + "";
            }
        }
        // Возвращает судоку с убранными числами
        ArrayList<ArrayList<Integer>> SudokuArray = removeCells(Level, AvailableIndexes);
        String Sudoku = "";
        for (ArrayList<Integer> row : SudokuArray) {
            for (Integer cell : row) {
                Sudoku += cell + "";
            }
        }
        return Sudoku;
    }

    private ArrayList<ArrayList<Integer>> removeCells(int Level, ArrayList<Integer> AvailableIndexes) {
        int RowIndex, ColumnIndex; // Хранение места клетки
        int OriginalValue; // Значение в клетке
        // Сколько необходимо убрать клеток
        int RemovedCells = 81 - Level - random.nextInt(10);
        int Upper = 0, Lower = 0;
        for (Integer Index : AvailableIndexes) {
            // Вычисляем положение клетки
            RowIndex = Index / 9;
            ColumnIndex = Index % 9;
            // Хранит значение клетки, прежде чем удалить
            OriginalValue = sudokuList.get(RowIndex).get(ColumnIndex);
            sudokuList.get(RowIndex).set(ColumnIndex, 0);
            // Уменьшить количество оставшихся для удаления клеток
            RemovedCells--;
            if (Index > 45) {
                Lower++;
            } else {
                Upper++;
            }
            // Проверить, что судоку имеет уникальное решение
            if (!hasUniqueSolution(Lower > Upper)) {
                // Если нет, то вернуть значение клетки
                sudokuList.get(RowIndex).set(ColumnIndex, OriginalValue);
                // Увеличить значение для клеток для удаления
                RemovedCells++;
                if (Index > 45) {
                    Lower--;
                } else {
                    Upper--;
                }
            }
            if (RemovedCells == 0) {
                return sudokuList;
            }
        }
        return sudokuList;
    }

    private ArrayList<ArrayList<Integer>> generateCompleteSudoku() {
        sudokuList.clear();
        for (int i = 0; i < 9; i++) {
            sudokuList.add(new ArrayList<>());
            for (int j = 0; j < 9; j++) {
                sudokuList.get(i).add(0);
            }
        }
        // Генерация судоку с первой клетке
        generateSudoku(0);
        return sudokuList;
    }

    private boolean generateSudoku(int Index) {
        // Неупорядоченные числа с 1 до 9.
        ArrayList<Integer> Values = getRandomValues(1, 9);
        // Вычисляем позицию клетки
        int x = Index / 9;
        int y = Index % 9;
        // Пробуем числа
        for (Integer Value : Values) {
            // Если это подходящее число
            if (ValidNumber(Value, x, y)) {
                // Установить значение в клетку
                sudokuList.get(x).set(y, Value);
                // Перейти к следующей клетке
                int next = Index + 1;
                if (Index == 80 || generateSudoku(next)) {
                    return true;
                }
            }
        }
        // Если нет подходящго числа, то вернуть false предыдущей клетке
        sudokuList.get(x).set(y, 0);
        return false;
    }

    private boolean hasUniqueSolution(boolean SearchForword) {
        // начало времени, чтобы отменить функцию, если длится больше 1 секунды
        startTime = System.currentTimeMillis();
        solutionCount = 0; // Число возможных решений
        if (SearchForword) {// начать поиск с первой или последней клетки
            hasUniqueSolution(0, 0, SearchForword);
        } else {
            hasUniqueSolution(8, 8, SearchForword);
        }
        return solutionCount == 1; // вернуть true, если решение единственно
    }

    private boolean hasUniqueSolution(int RowIndex, int ColumnIndex, boolean SearchForword) {
        int xLimit = 8, yLimit = 8; // последнее расположение ячеек, начиная с первой ячейки.
        if (!SearchForword) {
            // последнее расположение ячеек, начиная с последней ячейки.
            xLimit = 0;
            yLimit = 0;
        }
        // проверка если больше одного решения или функция выполнялась больше секунды
        if ((solutionCount > 1) || System.currentTimeMillis() - startTime > 1000) {
            return false;
        }
        // следующее число в клетке
        int nextCellNum = RowIndex * 9 + ColumnIndex + 1;
        if (!SearchForword) {
            nextCellNum -= 2;
        }
        // если клетка пуста
        if (sudokuList.get(RowIndex).get(ColumnIndex) == 0) {
            for (int PossibleNumber = 1; PossibleNumber < 10; PossibleNumber++) {
                // если подходящее число
                if (ValidNumber(PossibleNumber, RowIndex, ColumnIndex)) {
                    sudokuList.get(RowIndex).set(ColumnIndex, PossibleNumber);
                    // если это последняя ячейка, увеличить счетчик Solution, в противном случае перейти к следующей ячейке
                    if ((RowIndex == xLimit && ColumnIndex == yLimit) || hasUniqueSolution(nextCellNum / 9, nextCellNum % 9, SearchForword)) {
                        solutionCount++;
                        // false, чтобы продолжить поиск решений
                        return false;
                    }
                }
            }
            // если нет подходяшего числа, установить ноль
            sudokuList.get(RowIndex).set(ColumnIndex, 0);
            return false;// вернуться к предыдущей клетке
        } else // если это не последняя ячейка, перейдите к следующей ячейке.
            if (!(RowIndex == xLimit && ColumnIndex == yLimit)) {
                return hasUniqueSolution(nextCellNum / 9, nextCellNum % 9, SearchForword);
            } else {
                solutionCount++;
                return false;
            }
    }

    private boolean ValidNumber(int PossibleNumber, int RowIndex, int ColumnIndex) {
        // клетка в блоке
        int block_x = (RowIndex / 3) * 3;
        int block_y = (ColumnIndex / 3) * 3;
        // если число содержится в строке
        if (sudokuList.get(RowIndex).contains(PossibleNumber)) {
            return false;
        }
        // если число содержится в столбце
        if (sudokuList.stream().anyMatch(row -> row.get(ColumnIndex) == PossibleNumber)) {
            return false;
        }
        // если номер содержится в блоке
        for (int j = 0; j < 3; j++) {
            if (sudokuList.get(block_x + j).subList(block_y, block_y + 3).contains(PossibleNumber)) {
                return false;
            }
        }
        return true;
    }

    private ArrayList<Integer> getRandomValues(int minValue, int maxValue) {
        int count = maxValue - minValue + 1;
        ArrayList<Integer> randomValues = new ArrayList<>(count);
        IntStream.range(minValue, maxValue + 1).forEach(randomValues::add);
        IntStream.range(0, count).forEach(i -> randomValues.add(count - 1, randomValues.remove(random.nextInt(count - i))));
        return randomValues;
    }
}
