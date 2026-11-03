import { Box, Button, Card, CardContent, Divider, Typography } from '@mui/material';
import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router';
import { getActivityDetail } from '../services/api';

export const ActivityDetail = () => {
  const navigate = useNavigate();
  const {id} = useParams();
  const [activity, setActivity] = useState(null);
  const [recommendation, setRecommendation] = useState(null);

  useEffect(() => {
    const fetchActivityDetail = async () => {
      try {
        const response = await getActivityDetail(id);
        setActivity(response.data);
        setRecommendation(response.data.recommendation);
      } catch (error) {
        console.log(error);
      }
    }
    fetchActivityDetail();
  }, [id]);

  if (!activity) {
    return <Typography>Loading...</Typography>
  }

  return (
    <Box sx={{ maxWidth: 800, mx: 'auto', p: 2}}>
      <Button variant="contained" sx={{bgcolor: "#1f1f1f"}} onClick={() => navigate("/activities")}>Back</Button>
      <Card sx={{ mb: 2 }}>
        <CardContent>
          <Typography variant="h5" gutterBottom>Activity Details</Typography>
          <Typography>Type: {activity.type}</Typography>
          <Typography>Duration: {activity.duration}</Typography>
          <Typography>Calories: {activity.caloriesBurned}</Typography>
          <Typography>Date: {new Date(activity.createdAt).toLocaleString()}</Typography>
        </CardContent>
      </Card>
      {recommendation && (
        <Card>
          <CardContent>
            <Typography variant="h5" gutterBottom>AI Recommendation</Typography>
            <Typography variant="h6">Analysis</Typography>
            <Typography>{activity.recommendation}</Typography>

            <Divider sx={{ my: 2 }}/>

            <Typography variant="h6">Improvements</Typography>
            {activity?.improvements?.map((improvement, index)=> (
              <Typography key={index}>· {activity.improvements}</Typography>
            ))}

            <Divider sx={{ my: 2 }}/>

            <Typography variant="h6">Suggestions</Typography>
            {activity?.suggestions?.map((suggestion, index)=> (
              <Typography key={index}>· {suggestion}</Typography>
            ))}

            <Divider sx={{ my: 2 }}/>

            <Typography variant="h6">Safety Guidelines</Typography>
            {activity?.safetyMeasures?.map((safety, index)=> (
              <Typography key={index}>· {safety}</Typography>
            ))}
          </CardContent>
        </Card>
      )}
    </Box>
      
  )
}
