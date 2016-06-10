function(doc){
    if(doc.Type == "Time"){
        emit(doc.dayId);
    }
}