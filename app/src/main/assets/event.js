var s =  `
    <form name="newForm">
             <input  id="field-id"  disabled/>
             <input id="id-input" type="text"  placeholder="name" name="inputId">
                <br>

            <input id="id-result" type="text" name="result" disabled>
             <input id="id-btnResult" type="button" value="mostras" onClick = "show()"/>
                <table id="table-register">
                                <thead>
                                    <th>Name</th>
                                </thead>
                                <tbody id="tbody-data">

                                </tbody>
                            </table>
                            <input id="id-text" name="androidValue"></input>
    </form>
     `;

document.body.innerHTML = s;
function testMessage(){
    var text = document.getElementById("id-input").value
    var message = window.ob.testMessage(text);
    document.newForm.result.value = message;
    show();
    clearField();

}
function loadDataBase(){
    var db = openDatabase("testDB", "1.0", "JavaScript con SQLite", 2 * 1024 * 1024);
    db.transaction(function(tx) {
            tx.executeSql("CREATE TABLE IF NOT EXISTS USERS ( id INTEGER PRIMARY KEY,name TEXT)" );
          });
}
function show(){
    var table = document.getElementById('tbody-data');
    var miArray = new Array();
    var indexValue = 0;
     //alert(arr.join('\n'));
     indexValue= window.ob.getValueIndex();
     for(i=0; i<=indexValue; i++){
        miArray.push(window.ob.getFromAndroid(i));
     }
     var tr = '';
        for(var i = 0; i < miArray.length; i++){
                            tr += '<tr>';
                            tr += '<td >' + miArray[i]+ '</td>';
                            tr += '</tr>';
                    }
                        table.innerHTML = tr;
     alert(miArray);
    /* for(var i in miArray) {

        / console.log(miArray[i].join('\n')); // elemento actual
     }*/


}
/*

window.addEventListener('load', loading);

function loading(){

    document.getElementById('btn-add').addEventListener('click', save);
     document.getElementById('btn-delete').addEventListener('click', Adelete);
    db.transaction(function(tx) {
        tx.executeSql("CREATE TABLE IF NOT EXISTS USERS ( id INTEGER PRIMARY KEY,name TEXT)" );
      });
    show();

}
function save(){
    var id = document.getElementById('field-id').value;
    var name = document.getElementById('id-input').value;
    db.transaction(function(tx) {
      if(id){
            tx.executeSql('UPDATE users SET name=? WHERE id=?', [name,id]);
        }else{
            tx.executeSql('INSERT INTO users (name) VALUES (?)', [name]);
          }
    });
    show();
    clearField();

}
function show(){
    var table = document.getElementById('tbody-data');
    db.transaction(function(tx) {
        tx.executeSql('SELECT * FROM users', [], function (tx, resultado) {
            var rows = resultado.rows;
            var tr = '';
            for(var i = 0; i < rows.length; i++){
                    tr += '<tr>';
                    tr += '<td onClick="update(' + rows[i].id + ')">' + rows[i].name + '</td>';
                    tr += '</tr>';
            }
                table.innerHTML = tr;

        }, null);
    });
}
function update(_id){
     var id = document.getElementById('field-id');
    var name = document.getElementById('id-input');
    id.value = _id;
    db.transaction(function(tx) {
        tx.executeSql('SELECT * FROM users WHERE id=?', [_id], function (tx, resultado) {
            var rows = resultado.rows[0];
            name.value = rows.name ;
        });
    });
}

function Adelete(){

    var id = document.getElementById('field-id').value;

    db.transaction(function(tx) {
        tx.executeSql("DELETE FROM users WHERE id=?", [id]);
    });

    show();
    clearField();

}*/
function clearField(){

    //document.getElementById('field-id').value = '';
    document.getElementById('id-input').value = '';
}