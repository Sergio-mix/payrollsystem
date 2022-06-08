package co.edu.unbosque.payrollsystem.repository;

import co.edu.unbosque.payrollsystem.model.Record;
import co.edu.unbosque.payrollsystem.repository.jpa.RecordJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository(value = "RecordRepository")
public class RecordRepository {

    @Autowired
    private RecordJpa recordJpa;

    /**
     * Method that allows saving a record in the database
     *
     * @return object containing the log data
     */
    public Optional<Record> save(Record record) {
        return Optional.of(recordJpa.save(record));
    }

    /**
     * Method that allows to obtain all the records of the table
     *
     * @param name record name
     * @return List<RecordEntity> list of all the records of the table
     */
    public Optional<Record> getByName(String name) {
        return Optional.ofNullable(recordJpa.findByName(name));
    }
}
