package sqlStuff

import groovy.sql.Sql
class PlayWithMc {
    Sql connection = null

    PlayWithMc(Sql connection) {
        this.connection = connection
    }

    void select(int n) {
        connection.eachRow("SELECT * FROM catalogue_element LIMIT ${n}") {
            println it }
    }

    Boolean x() {
        Boolean b
        connection.query("SELECT COUNT(*) FROM catalogue_element"){resultSet ->
            b = resultSet.first()
        }
        return b
    }


    void deleteAttributes(Long id) {
        connection.execute("""
          DELETE FROM extension_value WHERE element_id=${id}
        """)

        connection.execute("""
          DELETE FROM enumerated_type WHERE id=${id}
        """)

        connection.execute("""
          DELETE FROM data_type WHERE id=${id}
        """)

        connection.execute("""
          DELETE FROM data_element WHERE id=${id}
        """)

        connection.execute("""
          DELETE FROM catalogue_element WHERE id=${id}
        """)

    }
//(1119..1180).each{id ->deleteAttributes(id)}
    void changeDM(Long fromModel, Long toModel) {
        connection.execute("""
      UPDATE catalogue_element SET data_model_id = ${toModel} WHERE data_model_id = ${fromModel}
    """)
    }
//changeDM(3793, 454)
    void changeDM(Long fromID, Long toID, Long toModel) {
        connection.execute("""
      UPDATE catalogue_element SET data_model_id = ${toModel} WHERE id >= ${fromID} AND id <= ${toID}
    """)
    }
//changeDM(3770, 3792, 3769)

    void changeModelName(Long modelID, String name) {
        connection.execute("""
      UPDATE catalogue_element SET name = ${name} WHERE id = ${modelID}
    """)
    }
//changeModelName(454, 'ClassesAndAttributes1')
    void processClassClassRelationship(Long classID) {
        String className = connection.firstRow("SELECT name FROM catalogue_element WHERE id=${classID}").name
        println className
        connection.eachRow("""
          SELECT * FROM extension_value WHERE
          element_id = ${classID} AND name like 'nhs-dd-relationship-%'
         """) {row ->
            println "${row.id}, ${row.extension_value}"
            String url = row.extension_value.find(/\[(.*?),/) {match, url -> url}
            println url
            String key = row.extension_value.find(/key:(.*?),/) {match, key -> key}
            println key
            String relDesc = row.extension_value.find(/key:.*, (.*)]/) {match, desc -> desc}
            println relDesc
            println ''

        }
    }

    List<Long> findRelationshipsWithSource(Long src) {
        List<Long> ids = []
        connection.eachRow("""SELECT * FROM relationship WHERE source_id = ${src}""") { row ->
            ids.add(row.id)
        }
        return ids
    }

    void deleteRelationshipsWithId(Long id) {
        connection.execute("""
          DELETE FROM relationship_metadata WHERE relationship_id=${id}
        """)
        connection.execute("""
          DELETE FROM relationship WHERE id=${id}
        """)
    }
//findRelationshipsWithSource(3988).each {rel -> deleteRelationshipsWithId(rel)}
/*(3770..3870).each {classID ->
    processClassClassRelationship(classID)
    println '\n==================\n'
}*/
/*
data_model_id=305
version=0
date_created=new Date()
description="Hello World..."
name="Hello World"
status="DRAFT"
sql.execute("""
   INSERT INTO catalogue_element( version, data_model_id,  date_created, description, last_updated, name, status, version_created, version_number)
   VALUES( ${version}, ${data_model_id}, ${date_created}, ${description}, ${date_created}, ${name}, ${status}, ${date_created}, 1)
""")

*/
}

