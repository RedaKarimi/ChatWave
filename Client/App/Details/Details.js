import React from "react";
import { Header } from "@rneui/themed";
import { View, Text, Button } from "react-native";

const Details = ({ navigation }) => {
  return (
    <View style={{ flex: 1, alignItems: "center", justifyContent: "center" }}>
      <Text>Details Screen</Text>
      <Button
        title="Go to Home"
        onPress={() => navigation.navigate("Home")}
        />
    </View>
  );
};

export default Details;
