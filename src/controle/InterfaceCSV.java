package controle;

import org.apache.commons.csv.CSVRecord;

public interface InterfaceCSV {
	
	public void setContinuaLeituraCSV(boolean terminou);
	public void addRegistroCSV(CSVRecord csvRecord);
}
