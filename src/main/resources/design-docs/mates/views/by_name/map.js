function(doc){
    if(doc.Type == "Mate"){
        emit([doc.surname, doc.name]);
    }
}