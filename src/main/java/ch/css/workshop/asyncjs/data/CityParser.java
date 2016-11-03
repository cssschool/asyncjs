package ch.css.workshop.asyncjs.data;



import java.util.ArrayList;
import java.util.List;

public class CityParser {

    private String previous = "";

    public List<CityData> parseNext(final String nextPart) {
        final ArrayList<CityData> result = new ArrayList<>();
        final String totalToParse = previous + nextPart;
        previous = "";


        final String[] rows = totalToParse.split("\n");
        final boolean endsWithEnter = nextPart.charAt(nextPart.length()-1) == '\n';
        final int lastRowIndex =  endsWithEnter ? rows.length - 1 : rows.length - 2;
        for ( int rowIndex =0; rowIndex<= lastRowIndex; rowIndex++) {
            final String row = rows[rowIndex];
            final String[] cols = row.split(",");
            if (cols.length == 7) {
                final CityData city = CityData.fromStrings(cols);
                result.add(city);
            } else {
                throw new IllegalStateException("cannot parse row:" + row);
            }
        }
        if ( !endsWithEnter) {
            previous = rows[rows.length-1];
        }
        return result;
    }
}
