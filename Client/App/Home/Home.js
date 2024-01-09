import React from "react";
import {
  StyleSheet,
  View,
  Text,
  Linking,
  StyleProp,
  Button,
  TextStyle,
  ViewStyle,
} from "react-native";
import { Header, HeaderProps, Icon } from "@rneui/themed";
import { TouchableOpacity } from "react-native-gesture-handler";

const Home = ({ navigation }) => {
  return (
    <View>
      <Header
        leftComponent={{
          icon: "menu",
          color: "#fff",
        }}
        rightComponent={
          <View style={styles.headerRight}>
            <TouchableOpacity>
              <Icon name="description" color="white" />
            </TouchableOpacity>
            <TouchableOpacity style={{ marginLeft: 10 }}>
              <Icon type="antdesign" name="rocket1" color="white" />
            </TouchableOpacity>
          </View>
        }
        centerComponent={{ text: "home", style: styles.heading }}
      />
      <View style={{ alignItems: "center", justifyContent: "center" }}>
        <Text>Home Screen</Text>
        <Button
          title="Go to Details"
          onPress={() => navigation.navigate("Details")}
        />
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  headerContainer: {
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "#397af8",
    marginBottom: 20,
    width: "100%",
    paddingVertical: 15,
  },
  heading: {
    color: "white",
    fontSize: 22,
    fontWeight: "bold",
  },
  headerRight: {
    display: "flex",
    flexDirection: "row",
    marginTop: 5,
  },
  subheaderText: {
    color: "white",
    fontSize: 16,
    fontWeight: "bold",
  },
});

export default Home;
