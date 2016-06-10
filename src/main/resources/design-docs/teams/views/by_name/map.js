function(doc){
    if(doc.Type == "Team"){
        emit(doc.name);
    }
}