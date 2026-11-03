import { Card, CardContent, Grid, Typography } from "@mui/material"
import { useEffect, useState } from "react";
import { useNavigate } from "react-router";
import { getActivities } from "../services/api";

type Activity = {
  id: string | number;
  type: string;
  durationMinutes: number | string;
  caloriesBurned: number | string;
};

export const ActivityList = () => {
  const [activities, setActivities] = useState<Activity[]>([]);
  const navigate = useNavigate();

  const fetchActivities = async () => {
    try {
      const response = await getActivities();
      setActivities(response.data as Activity[]);
    } catch (error) {
      console.error(error);
    }
  }

  useEffect(() => {
    fetchActivities();
  }, []);
  return (
    <Grid container spacing = {2}>
      {activities.map((activity) => (
        <Grid item xs={12} sm={6} md={4} key={activity.id}>
          <Card sx={{cursor: 'pointer'}} onClick={() => navigate(`/activities/${activity.id}`)}>
            <CardContent>
              <Typography variant='h6'>{activity.type}</Typography>
              <Typography>Duration: {activity.durationMinutes}</Typography>
              <Typography>Calories: {activity.caloriesBurned}</Typography>
            </CardContent>
          </Card>
        </Grid>
      ))}
    </Grid>
  )
}
