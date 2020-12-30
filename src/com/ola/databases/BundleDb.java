package com.ola.databases;

import com.ola.dataStructures.Bundle;
import com.ola.dataStructures.IdMap;

import java.util.ArrayList;
import java.util.HashMap;

public class BundleDb {
    private HashMap<String, Bundle> _bundles;
    private IdDb _idDbb;

    public BundleDb(Iterable<Bundle> bundles){
        _bundles = new HashMap<>();
        var idMaps = new ArrayList<IdMap>();
        for (var bundle:
             bundles) {
            _bundles.put(bundle.Id, bundle);
            idMaps.add(new IdMap(bundle.Id, bundle.Description));
        }
        _idDbb = new IdDb(idMaps, null);
    }
}
