'''
this script finds the adjacent provinces in original data

it produces a json adjacency matrix file

then finally checks the adjacency matrix you edited after is a fully connected graph and all connections are bidirectional

note the documentation for shapely (pip install shapely):
https://shapely.readthedocs.io/en/latest/manual.html?highlight=cascaded#object.touches

connections I made sure provided in updated adjacency file:
Britannia connects to Lugdunensis
Cyprus connects to Cilicia
Sardinia et Corsica connects to VII
Sicilia connects to III and Africa Proconsularis
Baetica connects to Mauretania Tingitana
Thracia connects to Birhynia et Pontus (already the case)

IMPORTANT:
you should manually generate the file province_adjacency_matrix_fully_connected.json by renaming province_adjacency_matrix.json and ensuring the graph is fully connected
'''

from collections import defaultdict
from shapely.geometry import MultiPolygon, Polygon
import json


M = defaultdict(dict)

WRITE_NEW_FILE = True

def flatten_list(m):
    return [e for l in m for e in l]

def depth_first_search(fully_connected_data, province_name, seen_set):
    cur_province = fully_connected_data[province_name]
    seen_set.add(province_name)
    for possibly_adjacent_province_name, is_adjacent in cur_province.items():
        if is_adjacent and possibly_adjacent_province_name not in seen_set:
            depth_first_search(fully_connected_data, possibly_adjacent_province_name, seen_set)
        

if __name__ == '__main__':
    with open('provinces_right_hand_fixed.geojson', 'r') as f:
        data = json.loads(f.read())

    multi_polygons = []
    province_names = []
        
    for feat in data['features']:
        polygons = []
        # if different type here, need to add to code since only handling MultiPolygons currently
        assert feat['geometry']['type'] == 'MultiPolygon'
        for poly in feat['geometry']['coordinates']:
            assert len(poly) == 1
            
            polygon = Polygon(poly[0])
            polygons.append(polygon)
        multi_polygons.append((feat['properties']['name'], MultiPolygon(polygons)))
        province_names.append(feat['properties']['name'])

    

    for i1, (name1, multi_poly1) in enumerate(multi_polygons):
        for i2, (name2, multi_poly2) in enumerate(multi_polygons):
            M[name1][name2] = False # pre-populate
            if i1 != i2:
                # note some overlap, e.g. Lugdunensis Belgica so can't assert not the case
                #assert(not multi_poly1.overlaps(multi_poly2)) # if overlap, data wrong
                if multi_poly1.intersects(multi_poly2) or multi_poly1.touches(multi_poly2):
                    M[name1][name2] = True

    M = dict(M)

    if WRITE_NEW_FILE:
        with open('province_adjacency_matrix.json', 'w') as f:
            f.write(json.dumps(M, indent=2))

    info = set()

    for name1, rel_info in M.items():
        for name2, is_intersecting in rel_info.items():
            if is_intersecting:
                info.add(tuple(sorted([name1, name2])))

    print('the following are the connections between provinces (note all are bidirectional):')
    for l in sorted(info):
        print(', '.join(l))

    print('\nthe following provinces do not connect to another province from starter data:')
    for l in sorted(set(province_names)-set(flatten_list(info))):
        print(l)

    

    # now do depth first search to check all provinces can be found from origin (in case some are disjoint but look adjacent)
    # you should manually generate the file province_adjacency_matrix_fully_connected.json by renaming province_adjacency_matrix.json and ensuring the graph is fully connected
    with open('province_adjacency_matrix_fully_connected.json', 'r') as f:
        fully_connected_data = json.loads(f.read())
    seen_set = set()
    depth_first_search(fully_connected_data, 'I', seen_set)  # start looking for provinces from I (Roman province)

    print('\nthe following provinces could not be found in DFS from province I:')
    if set(province_names) - seen_set:
        for p in sorted(set(province_names) - seen_set):
            print(p)

    # check directions bidirectional in revised json file (if fail assertion, made mistake in updating json file)
    for name1, rel_info in fully_connected_data.items():
        for name2 in rel_info:
            assert(fully_connected_data[name1][name2] == fully_connected_data[name2][name1])
    
