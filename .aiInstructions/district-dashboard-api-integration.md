

feature module -
district-dashboard (already exist)

domain module
districtDashboard (already exist)



Api endpoint ->
https://isam.sumato.tech/api/v1/district-dashboard
method -> get

add authorization header ->
`bearer 132|f99hRNrJODQsoyQrtkxSeVJpU16RURjd3BnCh7bR614505fe`


Response -> 
success
```
{
  "status": 200,
  "message": "Success",
  "data": {
    "districts": [
      {
        "id": 4,
        "name": "DIBANG VALLEY"
      },
      {
        "id": 3,
        "name": "CHANGLANG"
      },
      {
        "id": 27,
        "name": "Keyi Panyor"
      },
      {
        "id": 12,
        "name": "LONGDING"
      },
      {
        "id": 15,
        "name": "LOWER SUBANSIRI"
      },
      {
        "id": 24,
        "name": "UPPER SUBANSIRI"
      },
      {
        "id": 25,
        "name": "WEST KAMENG"
      }
    ],
    "stats": [
      {
        "label": "Total Schools",
        "value": 928,
        "description": "Across 7 districts."
      },
      {
        "label": "Internet Facility",
        "value": "3.34%",
        "description": "High-Speed FTTH / VSAT"
      },
      {
        "label": "MDM Kitchen Sheds",
        "value": "8.41%",
        "description": "Functional Hot Meals"
      },
      {
        "label": "Usable Classroom",
        "value": "63.86%",
        "description": "1804 out of 2825"
      }
    ],
    "school_category_list": [
      {
        "school_category": "Primary School",
        "class": "CLASS I - V",
        "total_schools": 494
      },
      {
        "school_category": "Secondary School",
        "class": "CLASS I - X",
        "total_schools": 49
      },
      {
        "school_category": "Upper Primary School",
        "class": "CLASS I - VIII",
        "total_schools": 288
      },
      {
        "school_category": "Higher Secondary School",
        "class": "CLASS IX - XII",
        "total_schools": 6
      },
      {
        "school_category": "Upper Primary School",
        "class": "CLASS VI - VIII",
        "total_schools": 6
      },
      {
        "school_category": "Higher Secondary School",
        "class": "CLASS I - XII",
        "total_schools": 15
      },
      {
        "school_category": "Higher Secondary School",
        "class": "CLASS VI - XII",
        "total_schools": 17
      },
      {
        "school_category": "Secondary School",
        "class": "CLASS VI - X",
        "total_schools": 15
      }
    ],
    "ongoing_projects": {
      "projects": [
        {
          "id": "01KR0DJEAHCDKNVJY28YXPSDE5",
          "project_name": "Infrastructure Development at Golden Jubilee School",
          "progress_percent": 0,
          "district_name": "LOWER SUBANSIRI",
          "updated_at": {
            "human": "4 hours ago",
            "date": "2026-07-29",
            "formatted": "Jul 29, 2026"
          }
        },
        {
          "id": "01KR0DJEASPD9T08XTEQQZ4FY5",
          "project_name": "Infrastructure Development at Golden Jubilee School",
          "progress_percent": 60,
          "district_name": "UPPER SUBANSIRI",
          "updated_at": {
            "human": "4 hours ago",
            "date": "2026-07-29",
            "formatted": "Jul 29, 2026"
          }
        },
        {
          "id": "01KR0DJE9CFQQ15K2VY9XA50BE",
          "project_name": "Infrastructure Development at Golden Jubilee School",
          "progress_percent": 0,
          "district_name": "WEST KAMENG",
          "updated_at": {
            "human": "4 hours ago",
            "date": "2026-07-29",
            "formatted": "Jul 29, 2026"
          }
        }
      ],
      "total_projects": 3
    }
  }
}
```
