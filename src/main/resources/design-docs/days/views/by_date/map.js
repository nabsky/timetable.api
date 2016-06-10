function(doc){
    if(doc.Type == "Day"){
        emit(doc.date);
    }
}