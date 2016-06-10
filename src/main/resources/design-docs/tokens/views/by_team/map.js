function(doc){
    if(doc.Type == "Token"){
        emit([doc.teamId, doc.leadMode]);
    }
}