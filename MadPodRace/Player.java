import java.util.*;

class Player {
    private static class Point {
        int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static class CheckPoint {
        private ArrayList<Point> listCheckPoint;
        private boolean firstTurn;
        
        public CheckPoint() {
            listCheckPoint = new ArrayList<>();
            firstTurn = true;
        } 

        public void checkCP(int x, int y) {
            boolean found = false;
            for (Point p : listCheckPoint) {
                if (p.x == x && p.y == y) {
                    System.err.println("OH !");
                    firstTurn = false;
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.err.println("push");
                listCheckPoint.add(new Point(x, y));
            }
        }

        public boolean bestBoost(int x, int y) {
            if (!firstTurn) {
                double maxDistance = -Double.MAX_VALUE;
                Point maxCP = new Point(0, 0);
                for (int i = 0; i < listCheckPoint.size(); i++) {
                    for (int j = 0; j < listCheckPoint.size(); j++) {
                        Point el1 = listCheckPoint.get(i);
                        Point el2 = listCheckPoint.get(j);
                        double d = distance(el1.x, el2.x, el1.y, el2.y);
                        if (d > maxDistance) {
                            maxDistance = d;
                            maxCP = el2;
                        }
                    }
                }
                System.err.println(maxCP);
                return maxCP.x == x && maxCP.y == y;
            }
            return false;
        }

        private double distance(int x1, int x2, int y1, int y2) {
            return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        }
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            CheckPoint cpm = new CheckPoint();
            boolean boost = false;
            int lastX = 0, lastY = 0;

            while (true) {
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                int nextCheckpointX = scanner.nextInt(); // x position of the next checkpoint
                int nextCheckpointY = scanner.nextInt(); // y position of the next checkpoint
                int nextCheckpointDist = scanner.nextInt(); // distance to the next checkpoint
                int nextCheckpointAngle = scanner.nextInt(); // angle between your pod orientation and the direction of the next checkpoint

                int opponentX = scanner.nextInt();
                int opponentY = scanner.nextInt();

                String text = "";
                String toDo = "100";

                if (useShield(opponentX, opponentY, x, y, nextCheckpointAngle)) {
                    toDo = "SHIELD";
                    text = " SAVE MEEEE";
                } else if (!boost && cpm.bestBoost(nextCheckpointX, nextCheckpointY) && Math.abs(nextCheckpointAngle) < 2) {
                    toDo = "BOOST";
                    text = " LET'S GO";
                    boost = true;
                } else if (Math.abs(nextCheckpointAngle) > 90) {
                    toDo = "0";
                    text = " BAD ANGLE!!!! WATCH OUT!!!";
                }

                if (text.isEmpty()) {
                    text = " " + toDo;
                }

                if (nextCheckpointX != lastX || nextCheckpointY != lastY) {
                    cpm.checkCP(nextCheckpointX, nextCheckpointY);
                    lastX = nextCheckpointX;
                    lastY = nextCheckpointY;
                }

                System.out.println(nextCheckpointX + " " + nextCheckpointY + " " + toDo + text);
            }
        }
    }

    private static boolean useShield(int opponentX, int opponentY, int X, int Y, int myAngle) {
        double angle = Math.atan2(opponentY - Y, opponentX - X);
        double angleDeg = Math.toDegrees(angle);
        return Math.abs(myAngle - angleDeg) >= 5 && distance(opponentX, X, opponentY, Y) <= 500;
    }

    private static double distance(int x1, int x2, int y1, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}