function(doc){
    if(doc.Type == "Unit"){
        emit(doc.name);
    }
}