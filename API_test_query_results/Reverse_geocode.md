### GET https://api.twitter.com/1.1/geo/reverse_geocode.json?lat=37.781157&long=-122.398720&granularity=neighborhood

### Result (JSON):

```
{
    "query": {
        "url": "https://api.twitter.com/1.1/geo/reverse_geocode.json?lat=37.781157&long=-122.398720&granularity=neighborhood",
        "type": "reverse_geocode",
        "params": {
            "accuracy": 0.0,
            "coordinates": {
                "coordinates": [
                    -122.39872,
                    37.781157
                ],
                "type": "Point"
            },
            "granularity": "neighborhood"
        }
    },
    "result": {
        "places": [
            {
                "id": "5a110d312052166f",
                "name": "San Francisco",
                "full_name": "San Francisco, CA",
                "country": "United States",
                "country_code": "US",
                "url": "https://api.twitter.com/1.1/geo/id/5a110d312052166f.json",
                "place_type": "city",
                "attributes": {},
                "bounding_box": {
                    "type": "Polygon",
                    "coordinates": [
                        [
                            [
                                -122.514926,
                                37.708075
                            ],
                            [
                                -122.514926,
                                37.833238
                            ],
                            [
                                -122.357031,
                                37.833238
                            ],
                            [
                                -122.357031,
                                37.708075
                            ],
                            [
                                -122.514926,
                                37.708075
                            ]
                        ]
                    ]
                },
                "centroid": [
                    -122.4461400159226,
                    37.759828999999996
                ],
                "contained_within": [
                    {
                        "id": "5122804691e5fecc",
                        "name": "SAN FRANCISCO-OAK-SAN JOSE",
                        "full_name": "SAN FRANCISCO-OAK-SAN JOSE",
                        "country": "",
                        "country_code": "",
                        "url": "https://api.twitter.com/1.1/geo/id/5122804691e5fecc.json",
                        "place_type": "admin",
                        "attributes": {},
                        "bounding_box": {
                            "type": "Polygon",
                            "coordinates": [
                                [
                                    [
                                        -124.022819,
                                        36.893329
                                    ],
                                    [
                                        -124.022819,
                                        40.002141
                                    ],
                                    [
                                        -121.208156,
                                        40.002141
                                    ],
                                    [
                                        -121.208156,
                                        36.893329
                                    ],
                                    [
                                        -124.022819,
                                        36.893329
                                    ]
                                ]
                            ]
                        },
                        "centroid": [
                            -122.62595430012564,
                            38.447735
                        ]
                    }
                ]
            },
            {
                "id": "2b6ff8c22edd9576",
                "name": "SoMa",
                "full_name": "SoMa, San Francisco",
                "country": "United States",
                "country_code": "US",
                "url": "https://api.twitter.com/1.1/geo/id/2b6ff8c22edd9576.json",
                "place_type": "neighborhood",
                "attributes": {},
                "bounding_box": {
                    "type": "Polygon",
                    "coordinates": [
                        [
                            [
                                -122.42284884,
                                37.76893497
                            ],
                            [
                                -122.42284884,
                                37.78752897
                            ],
                            [
                                -122.3964,
                                37.78752897
                            ],
                            [
                                -122.3964,
                                37.76893497
                            ],
                            [
                                -122.42284884,
                                37.76893497
                            ]
                        ]
                    ]
                },
                "centroid": [
                    -122.40848289157051,
                    37.77823196999999
                ],
                "contained_within": [
                    {
                        "id": "5a110d312052166f",
                        "name": "San Francisco",
                        "full_name": "San Francisco, CA",
                        "country": "United States",
                        "country_code": "US",
                        "url": "https://api.twitter.com/1.1/geo/id/5a110d312052166f.json",
                        "place_type": "city",
                        "attributes": {},
                        "bounding_box": {
                            "type": "Polygon",
                            "coordinates": [
                                [
                                    [
                                        -122.514926,
                                        37.708075
                                    ],
                                    [
                                        -122.514926,
                                        37.833238
                                    ],
                                    [
                                        -122.357031,
                                        37.833238
                                    ],
                                    [
                                        -122.357031,
                                        37.708075
                                    ],
                                    [
                                        -122.514926,
                                        37.708075
                                    ]
                                ]
                            ]
                        },
                        "centroid": [
                            -122.4461400159226,
                            37.759828999999996
                        ]
                    }
                ]
            },
            {
                "id": "fbd6d2f5a4e4a15e",
                "name": "California",
                "full_name": "California, USA",
                "country": "United States",
                "country_code": "US",
                "url": "https://api.twitter.com/1.1/geo/id/fbd6d2f5a4e4a15e.json",
                "place_type": "admin",
                "attributes": {},
                "bounding_box": {
                    "type": "Polygon",
                    "coordinates": [
                        [
                            [
                                -124.482003,
                                32.528832
                            ],
                            [
                                -124.482003,
                                42.009519
                            ],
                            [
                                -114.131212,
                                42.009519
                            ],
                            [
                                -114.131212,
                                32.528832
                            ],
                            [
                                -124.482003,
                                32.528832
                            ]
                        ]
                    ]
                },
                "centroid": [
                    -120.02487379467527,
                    37.2691755
                ],
                "contained_within": [
                    {
                        "id": "96683cc9126741d1",
                        "name": "United States",
                        "full_name": "United States",
                        "country": "United States",
                        "country_code": "US",
                        "url": "https://api.twitter.com/1.1/geo/id/96683cc9126741d1.json",
                        "place_type": "country",
                        "attributes": {},
                        "bounding_box": {
                            "type": "Polygon",
                            "coordinates": [
                                [
                                    [
                                        -179.231086,
                                        13.182335
                                    ],
                                    [
                                        -179.231086,
                                        71.434357
                                    ],
                                    [
                                        179.859685,
                                        71.434357
                                    ],
                                    [
                                        179.859685,
                                        13.182335
                                    ],
                                    [
                                        -179.231086,
                                        13.182335
                                    ]
                                ]
                            ]
                        },
                        "centroid": [
                            -98.99308143101959,
                            36.890333500000004
                        ]
                    }
                ]
            },
            {
                "id": "96683cc9126741d1",
                "name": "United States",
                "full_name": "United States",
                "country": "United States",
                "country_code": "US",
                "url": "https://api.twitter.com/1.1/geo/id/96683cc9126741d1.json",
                "place_type": "country",
                "attributes": {},
                "bounding_box": {
                    "type": "Polygon",
                    "coordinates": [
                        [
                            [
                                -179.231086,
                                13.182335
                            ],
                            [
                                -179.231086,
                                71.434357
                            ],
                            [
                                179.859685,
                                71.434357
                            ],
                            [
                                179.859685,
                                13.182335
                            ],
                            [
                                -179.231086,
                                13.182335
                            ]
                        ]
                    ]
                },
                "centroid": [
                    -98.99308143101959,
                    36.890333500000004
                ],
                "contained_within": []
            }
        ]
    }
}
```
