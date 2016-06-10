function(doc){
    if(doc.Type == "Mate"){
        emit(doc.unitId);
    }
}