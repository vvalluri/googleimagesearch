Please review Assignment 2: Google Image Search App



User stories Completed:


1. User can enter a search query that will display a grid of image results from the Google Image API.

2. User can click on "settings" which allows selection of advanced search options to filter results. I have used "filter" icon to trigger this in the SherlockActionBar

3. User can configure advanced search filters such as:
   - Size (small, medium, large, extra-large)
   - Color filter (black, blue, brown, gray, green, etc...)
   - Type (faces, photo, clip art, line art)
   - Site (espn.com)
   
4. Subsequent searches have any filters applied to the search results

5. User can click on any image in results to see the image full-screen

6. User can scroll down “infinitely” to continue loading more image results (up to 8 pages)

The following advanced user stories are also completed:

1. Advanced: Robust error handling, check if internet is available, handle error cases, network failures
2. Advanced: Used SherlockActionBar SearchView as the query box instead of an EditText
3. Advanced: User can share an image to their friends or email it to themselves
4. Advanced: Replaced Filter Settings Activity with a lightweight modal overlay
5. Advanced: Improve the user interface and experiment with image assets and/or styling and coloring - Adjusted the image size and the Title size in the StaggeredGridView. Set background text for title
6. Bonus: Used StaggeredGridView to display improve the grid of image results. STill playing with image size ratios
7. Bonus: User can zoom or pan images displayed in full-screen detail view. This i could test on device but emulator has some issues.

GIF walkthrough:

![alt tag](https://github.com/vvalluri/googleimagesearch/blob/master/codepath-googleimagesearch-1.gif)
